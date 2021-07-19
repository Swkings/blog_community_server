package com.swking.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/17
 * @File : SensitiveUtil
 * @Desc : 敏感词过滤
 **/

@Slf4j
@Component
public class SensitiveUtil {
    // Trie节点
    private class TrieNode {
        // 词尾标识
        private boolean keyEnd;
        // 子节点
        private Map<Character, TrieNode> next;

        TrieNode(){
            keyEnd = false;
            next = new HashMap<>();
        }
        public boolean isKeyEnd(){
            return keyEnd;
        }
        public void setEnd(boolean end){
            keyEnd = end;
        }

        public TrieNode getNext(Character ch){
            return next.get(ch);
        }

        public void addNode(Character ch, TrieNode node){
            next.put(ch, node);
        }
    }

    // 替换词
    private static final String REPLACEMENT = "***";

    // 根节点为空
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try (
                // 放在try括号里，文件流会自动关闭，不用手动关
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("static/sensitive-words/main.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyWord;
            while ((keyWord = reader.readLine()) != null) {
                // 添加到前缀树
                this.insert(keyWord);
            }
        } catch (IOException e) {
            log.error("加载敏感词文件失败: " + e.getMessage());
        }
    }

    private void insert(String str){
        /**
         * 插入词
         * */
        TrieNode node = rootNode;
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            TrieNode next = node.getNext(ch);
            if (next == null) {
                next = new TrieNode();
                node.addNode(ch, next);
            }
            node = next;
        }
        node.setEnd(true);
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    public String filter(String text) {
        /**
         * 过滤敏感词
         * @param text 待过滤的文本
         * @return 过滤后的文本
         */
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();
        int textLen = text.length();
        char ch;
        while (position < textLen) {
            ch = text.charAt(position);
            // 跳过符号
            if (isSymbol(ch)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(ch);
                    begin++;
                }
                // 无论符号在开头或中间,指针3都向下走一步
                position++;
                continue;
            }
            // 检查下级节点
            tempNode = tempNode.getNext(ch);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeyEnd()) {
                // 发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                position++;
            }
        }
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }


}
