package dev.wuason.mechanics.invmechanic.types.pages.content.multiple;

public class Content {
    private Object content;
    private PageCustomInfo<?> pageCustomInfo;

    public Content(Object content, PageCustomInfo<?> pageCustomInfo) {
        this.content = content;
        this.pageCustomInfo = pageCustomInfo;
    }

    public Object getContent() {
        return content;
    }

    public PageCustomInfo<?> getPageCustomInfo() {
        return pageCustomInfo;
    }
}
