import com.example.wearit.model.Category
import com.example.wearit.model.Item

fun getItemMap(itemList: List<Item>): Map<Category, List<Item>> {
    val sortedItemList = itemList.sortedWith(compareBy({ it.category }, { it.id }))

    val itemsMap = mutableMapOf<Category, MutableList<Item>>()

    sortedItemList.forEach { item ->
        if (itemsMap.containsKey(item.category)) {
            itemsMap.getValue(item.category).add(item)
        } else {
            itemsMap[item.category] = mutableListOf(item)
        }
    }

    return itemsMap
}

fun getNextItem(itemsInCategory: List<Item>, currentItemId: Int, next: Boolean): Int? {
    val activeItemsWithCurrentItem = itemsInCategory
        .filter { it.isActive || it.id == currentItemId }

    val currentItemIndex = activeItemsWithCurrentItem.indexOfFirst { it.id == currentItemId }
    var newCurrentItemIndex = currentItemIndex + if (next) 1 else -1

    if (newCurrentItemIndex == activeItemsWithCurrentItem.size)
        newCurrentItemIndex = 0
    else if (newCurrentItemIndex < 0)
        newCurrentItemIndex = activeItemsWithCurrentItem.size - 1

    return if (activeItemsWithCurrentItem[newCurrentItemIndex].isActive)
        activeItemsWithCurrentItem[newCurrentItemIndex].id
    else
        null
}

fun getDrawnItems(items: List<Item>): List<Int> {
    val itemMap: Map<Category, List<Item>> = getItemMap(items)

    val newCurrentSelection = mutableListOf<Int>()

    itemMap.forEach { entry ->
        val listOfActiveItems = entry.value.filter { it.isActive }
        if (listOfActiveItems.isNotEmpty()) {
            newCurrentSelection.add(listOfActiveItems.random().id)
        }
    }

    return newCurrentSelection
}