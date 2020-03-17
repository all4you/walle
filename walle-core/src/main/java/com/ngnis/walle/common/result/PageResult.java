package com.ngnis.walle.common.result;

public class PageResult<T> extends BaseResult {

    private static final long serialVersionUID = 4523024906338896795L;
    private Page<T> content;

    public PageResult() {
    }

    public Page<T> getContent() {
        return this.content;
    }

    public void setContent(Page<T> content) {
        this.content = content;
    }
}
