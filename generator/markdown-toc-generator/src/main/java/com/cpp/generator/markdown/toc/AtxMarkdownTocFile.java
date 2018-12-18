package com.cpp.generator.markdown.toc;

import com.github.houbb.markdown.toc.core.impl.AtxMarkdownToc;

/**
 * markdown toc generator
 *
 * @author chenjian
 * @date 2018-12-18 17:46
 */
public class AtxMarkdownTocFile {

    public static void main(String[] args) {
        AtxMarkdownToc.newInstance().genTocFile("/Users/chenjian/Desktop/mygit/sty/note/Mysql/索引.md");
    }
}
