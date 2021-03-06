package com.store.v1.items.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.v1.groups.domain.Groups;
import com.store.v1.items.domain.Items;
import com.store.v1.items.domain.ItemsList;
import com.store.v1.items.service.ItemsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.Optional;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static com.store.common.StoreConstants.STORE_BASE_URL;
import static com.store.v1.groups.utils.GroupsTestUtils.generateGroup;
import static com.store.v1.items.utils.ItemsTestUtils.generateItem;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class ItemsControllerTest {

    private static final String ITEMS_URL = "/" + STORE_BASE_URL + "/items";

    private ItemsController itemsController;

    @Mock
    private ItemsService itemsService;

    private MockMvc mockMvc;
    private ObjectMapper jacksonObjectMapper;
    private String id;
    private Items items;
    private String groupId;
    private Groups groups;

    @Before
    public void setUp() throws Exception {
        itemsController = new ItemsController(itemsService);

        mockMvc = MockMvcBuilders.standaloneSetup(itemsController).build();
        jacksonObjectMapper = new ObjectMapper();
        jacksonObjectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        id = randomUUID().toString();
        groupId = randomUUID().toString();
        items = generateItem(id, Optional.empty(), groupId, Optional.of(new Date()));
        groups = generateGroup(groupId, Optional.empty());
    }

    @Test
    public void testSave() throws Exception {
        Optional<Date> date = Optional.of(new Date());
        Items newItems = generateItem(null, Optional.empty(), groupId, date);
        Items savedItems = generateItem(id, Optional.empty(), groupId, date);

        when(itemsService.save(newItems)).thenReturn(savedItems);

        String request = jacksonObjectMapper.writeValueAsString(newItems);
        String response = jacksonObjectMapper.writeValueAsString(savedItems);

        mockMvc.perform(post(ITEMS_URL)
                .content(request)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    public void testUpdate() throws Exception {
        items.setName("Updated name");
        String request = jacksonObjectMapper.writeValueAsString(groups);

        mockMvc.perform(put(ITEMS_URL + "/" + id)
                .content(request)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGet() throws Exception {
        when(itemsService.get()).thenReturn(new ItemsList(singletonList(items)));

        String response = jacksonObjectMapper.writeValueAsString(itemsService.get().getItems());

        mockMvc.perform(get(ITEMS_URL))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    public void getById() throws Exception {
        when(itemsService.findById(id)).thenReturn(Optional.of(items));

        String response = jacksonObjectMapper.writeValueAsString(itemsService.findById(id).get());

        mockMvc.perform(get(ITEMS_URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    public void testGetByGroupId() throws Exception {
        when(itemsService.findByGroupId(id)).thenReturn(emptyList());

        String response = jacksonObjectMapper.writeValueAsString(itemsService.findByGroupId(id));

        mockMvc.perform(get(ITEMS_URL + "/getByGroupId/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(ITEMS_URL + "/" + id)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}