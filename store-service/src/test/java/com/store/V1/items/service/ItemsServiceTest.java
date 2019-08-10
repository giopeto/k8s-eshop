package com.store.V1.items.service;


import com.store.V1.items.domain.Items;
import com.store.V1.items.repository.ItemsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.store.V1.items.utils.ItemsTestUtils.generateItem;
import static com.store.V1.items.utils.ItemsTestUtils.generateItems;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ItemsServiceTest {

	private ItemsService itemsService;

	@Mock private ItemsRepository itemsRepository;

	private String itemId;
	private String groupId;
	private Items item;

	@Before
	public void setUp() throws Exception {
		itemsService = new ItemsServiceImpl(itemsRepository);
		itemId = randomUUID().toString();
		groupId = randomUUID().toString();

		item = generateItem(null, Optional.empty(), groupId, Optional.empty());
	}

	@Test
	public void testSave() throws Exception {
		Items savedItems = generateItem(itemId, Optional.of(item.getName()), item.getGroupId(), Optional.empty());

		when(itemsRepository.save(item)).thenReturn(savedItems);

		Items resultItems = itemsService.save(item);
		assertEquals(itemId, resultItems.getId());
	}

	@Test
	public void testGet() throws Exception {
		List allItems = generateItems(2, groupId);

		when(itemsRepository.findAll()).thenReturn(allItems);

		assertEquals(itemsService.get().getItems(), allItems);
	}

	@Test
	public void testFindById() throws Exception {
		when(itemsRepository.findById(itemId)).thenReturn(Optional.of(item));

		assertEquals(itemsService.findById(itemId).get(), item);
	}

	@Test
	public void testFindByGroupId() throws Exception {
		List allItems = generateItems(2, groupId);

		when(itemsRepository.findByGroupId(groupId)).thenReturn(allItems);

		assertEquals(itemsService.findByGroupId(groupId).getItems(), allItems);
	}

	@Test
	public void testDelete() throws Exception {
		//when(itemsRepository.deleteById(itemId)).thenReturn(result);

		//assertEquals(itemsService.delete(itemId), result);
	}

}