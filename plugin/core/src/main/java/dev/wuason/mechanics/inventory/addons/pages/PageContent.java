package dev.wuason.mechanics.inventory.addons.pages;

import java.util.List;

public interface PageContent<T> extends Pageable {

    List<T> getContent();

    void setContent(List<T> content);

    void addContent(T content);

    void removeContent(T content);

    void clearContent();

    void addAllContent(List<T> content);
}
