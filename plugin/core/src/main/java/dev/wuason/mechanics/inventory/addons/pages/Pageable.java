package dev.wuason.mechanics.inventory.addons.pages;

public interface Pageable {

    public void handlePreviousPage(PageButton pageButton);

    public void handleNextPage(PageButton pageButton);

    public int getCurrentPage();

    public void setCurrentPage(int currentPage);

    public int getMaxPage();

    public int getMinPage();

    public void updatePage();

    public void nextPage();

    public void previousPage();

}
