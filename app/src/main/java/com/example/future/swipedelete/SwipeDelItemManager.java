package com.example.future.swipedelete;

/**
 * Author: Future <br>
 * QQ: <br>
 * Description:管理打开的 SwipeDelItem 实例<br>
 * date: 2016/10/26  17:26.
 */

public class SwipeDelItemManager {
    private SwipeDeleteItem openedItem;

    private SwipeDelItemManager() {
    }

    public static SwipeDelItemManager getInstance() {
        return Holder.instance;
    }

    public void registerSwipeItem(SwipeDeleteItem item) {
        openedItem = item;
    }

    public boolean isSwipeItemNull() {
        return openedItem == null;
    }

    public SwipeDeleteItem getOpenedItem() {
        return openedItem;
    }

    public void unRegisterSwipeItem(SwipeDeleteItem item) {
        if (openedItem == item) {
            openedItem = null;
        }
    }

    private static class Holder {
        private static SwipeDelItemManager instance = new SwipeDelItemManager();
    }
}
