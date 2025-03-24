package com.jeemoo.system.param;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuTree {

    private Long id;

    private String name;

    private String icon;

    private String menuType;

    private Long parentId;

    private List<MenuTree> subMenu;

    public static List<MenuTree> buildCategoryTree(List<MenuTree> categories) {
        List<MenuTree> tree = new ArrayList<>();

        for (MenuTree category : categories) {
            boolean parentExists = false; // 标志用于判断父级是否存在

            if (category.getParentId() == 0) {
                tree.add(category);
                parentExists = true;
            } else {
                for (MenuTree parentCategory : categories) {
                    if (parentCategory.getId().equals(category.getParentId())) {
                        parentCategory.addChild(category);
                        parentExists = true; // 父级存在的标志设为 true
                        break;
                    }
                }
            }

            if (!parentExists) {
                tree.add(category); // 当父级不存在时，将当前节点作为一级节点添加到树中
            }
        }

        return tree;
    }

    public void addChild(MenuTree child) {
        if (subMenu == null) subMenu = new ArrayList<>();
        subMenu.add(child);
    }
}
