package com.project.changzhzfinalproject;

public class CatalogMgm {
    private String catalog, description;

    /**
     * Catalog Management constructor
     *
     * @param catalog
     * @param description
     */
    public CatalogMgm(String catalog, String description){
        this.catalog=catalog;
        this.description=description;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
