package com.example.wearit.data

import com.example.wearit.model.Category
import com.example.wearit.model.Item
import getDrawnItems
import getItemMap
import getNextItem
import org.junit.Before
import org.junit.Test

class UtilsTest {

    private lateinit var testItems: List<Item>

    @Before
    fun before() {
        testItems = mutableListOf<Item>(
            Item(
                id = 1,
                name = "item-1",
                photoFilename = "item-1.png",
                category = Category.Headgear
            ),
            Item(
                id = 2,
                name = "item-2",
                photoFilename = "item-2.png",
                category = Category.Headgear
            ),
            Item(
                id = 3,
                name = "item-3",
                photoFilename = "item-3.png",
                category = Category.Tshirt
            ),
            Item(
                id = 4,
                name = "item-4",
                photoFilename = "item-4.png",
                category = Category.Shorts
            )
        )
    }

    @Test
    fun `transform list of items into map`() {
        val result = getItemMap(testItems)

        val expectedMap: Map<Category, List<Item>> = mapOf(
            Category.Headgear to listOf(testItems[0], testItems[1]),
            Category.Tshirt to listOf(testItems[2]),
            Category.Shorts to listOf(testItems[3])
        )

        assert(result == expectedMap)
    }

    @Test
    fun `transform unordered list of items into map`() {
        val unorderedItems = testItems.shuffled()

        val result = getItemMap(unorderedItems)

        val expectedMap: Map<Category, List<Item>> = mapOf(
            Category.Headgear to listOf(testItems[0], testItems[1]),
            Category.Tshirt to listOf(testItems[2]),
            Category.Shorts to listOf(testItems[3])
        )

        assert(result == expectedMap)
    }

    @Test
    fun `create map from empty list`() {
        val result = getItemMap(listOf())

        val expectedMap = mapOf<Category, List<Item>>()

        assert(result == expectedMap)
    }

    @Test
    fun `select next item in category`() {
        val itemsInCategory = testItems.filter { it.category == Category.Headgear }

        val result = getNextItem(itemsInCategory = itemsInCategory, currentItemId = itemsInCategory[0].id, next = true)

        val expectedId = itemsInCategory[1].id

        assert(result == expectedId)
    }

    @Test
    fun `select previous item in category`() {
        val itemsInCategory = testItems.filter { it.category == Category.Headgear }

        val result = getNextItem(itemsInCategory = itemsInCategory, currentItemId = itemsInCategory[1].id, next = false)

        val expectedId = itemsInCategory[0].id

        assert(result == expectedId)
    }

    @Test
    fun `do not change item if only one item in category`() {
        val itemsInCategory = testItems.filter { it.category == Category.Headgear }.toMutableList()
        itemsInCategory.removeAt(1)

        val result = getNextItem(itemsInCategory = itemsInCategory, currentItemId = itemsInCategory[0].id, next = true)

        val expectedId = itemsInCategory[0].id

        assert(result == expectedId)
    }

    @Test
    fun `do not change item if only one active item in category`() {
        val itemsInCategory = testItems.filter { it.category == Category.Headgear }
        itemsInCategory[1].isActive = false

        val result = getNextItem(itemsInCategory = itemsInCategory, currentItemId = itemsInCategory[0].id, next = true)

        val expectedId = itemsInCategory[0].id

        assert(result == expectedId)
    }

    @Test
    fun `return null if no active items in category`() {
        val itemsInCategory = testItems.filter { it.category == Category.Headgear }.map { it.isActive = false; it }

        val result = getNextItem(itemsInCategory = itemsInCategory, currentItemId = itemsInCategory[0].id, next = true)

        val expectedId = null

        assert(result == expectedId)
    }

    @Test
    fun `draw items`() {
        val result = getDrawnItems(testItems)

        assert(result.isNotEmpty())
    }

    @Test
    fun `draw empty list if no items`() {
        val result = getDrawnItems(listOf())

        assert(result.isEmpty())
    }

    @Test
    fun `do not draw inactive item`() {
        testItems[2].isActive = false

        val result = getDrawnItems(testItems)

        assert(!result.contains(testItems[2].id))
    }

    @Test
    fun `draw empty list if no active items`() {
        testItems.forEach { it.isActive = false }

        val result = getDrawnItems(testItems)

        assert(result.isEmpty())
    }
}