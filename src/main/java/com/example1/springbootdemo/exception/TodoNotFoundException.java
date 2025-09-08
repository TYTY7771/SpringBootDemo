package com.example1.springbootdemo.exception;

/**
 * Todo不存在异常
 * 当查询、更新或删除不存在的Todo时抛出此异常
 */
public class TodoNotFoundException extends RuntimeException {

    /**
     * 默认构造函数
     */
    public TodoNotFoundException() {
        super("Todo任务不存在");
    }

    /**
     * 带消息的构造函数
     * @param message 异常消息
     */
    public TodoNotFoundException(String message) {
        super(message);
    }

    /**
     * 带ID的构造函数
     * @param id Todo的ID
     */
    public TodoNotFoundException(Long id) {
        super("ID为 " + id + " 的Todo任务不存在");
    }

    /**
     * 带消息和原因的构造函数
     * @param message 异常消息
     * @param cause 异常原因
     */
    public TodoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}