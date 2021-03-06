package com.store.v1.items.service;

import com.store.common.CookieParser;
import com.store.jms.producer.JmsProducer;
import com.store.v1.items.domain.Items;
import com.store.v1.items.domain.ItemsDto;
import com.store.v1.items.repository.ItemsRepository;
import com.store.v1.remote.call.files.FilesClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.store.common.StoreConstants.JSESSIONID;
import static com.store.v1.items.utils.ItemsTestUtils.generateItem;
import static com.store.v1.items.utils.ItemsTestUtils.generateItems;
import static java.util.UUID.randomUUID;
import static org.assertj.core.util.Lists.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ItemsServiceTest {

    private ItemsService itemsService;

    @MockBean
    private ItemsRepository itemsRepository;
    @MockBean
    private FilesClient filesClient;
    @MockBean
    private CookieParser cookieParser;

    @MockBean
    private JmsProducer jmsProducer;

    private String itemId;
    private String groupId;
    private Items item;

    @Before
    public void setUp() throws Exception {
        itemsService = new ItemsServiceImpl(itemsRepository, filesClient, cookieParser, jmsProducer);
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
        List<Items> allItems = generateItems(2, groupId);

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
        List<Items> allItems = generateItems(2, groupId);
        List<ItemsDto> allItemsDto = allItems.stream().map(item -> mapToItemsDto(item, emptyList())).collect(Collectors.toList());

        when(cookieParser.getCookie(JSESSIONID)).thenReturn(Optional.of("123-456"));
        when(itemsRepository.findByGroupId(groupId)).thenReturn(allItems);

        assertEquals(itemsService.findByGroupId(groupId), allItemsDto);
    }

    private ItemsDto mapToItemsDto(Items item, List<String> fileIdsPerItem) {
        return new ItemsDto(item.getId(),
                item.getName(),
                item.getGroupId(),
                item.getShortDescription(),
                item.getDescription(),
                item.getPrice(),
                item.isArchive(),
                fileIdsPerItem);
    }

}