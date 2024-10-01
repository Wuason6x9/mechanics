package dev.wuason.mechanics.inventory.pages;

import dev.wuason.mechanics.inventory.items.def.pages.PageButton;

public interface Pageable {

    public void handlePreviousPage(PageButton pageButton);

    public void handleNextPage(PageButton pageButton);

    public int getCurrentPage();

    public void setCurrentPage(int currentPage);

    public int getMaxPage();

    public int getMinPage();

    public void updatePage();

    public boolean nextPage();

    public boolean previousPage();

}
