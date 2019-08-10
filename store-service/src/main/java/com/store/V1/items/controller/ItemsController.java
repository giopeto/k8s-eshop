package com.store.V1.items.controller;

import com.store.V1.items.domain.Items;
import com.store.V1.items.service.ItemsService;
import com.store.V1.security.SecurityClient;
import com.store.common.StoreConstants;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@AllArgsConstructor
@RequestMapping(StoreConstants.STORE_BASE_URL + "/items")
public class ItemsController {

    @NonNull
    private final ItemsService itemsService;

    @NonNull
    private final SecurityClient securityClient;


    @RequestMapping(method = POST)
    public Items save(@RequestBody Items items) {
        return itemsService.save(items);
    }

    @RequestMapping(value = "{id}", method = PUT)
    public Items update(@PathVariable String id, @RequestBody Items items) {
        return itemsService.save(items);
    }

    @RequestMapping(method = GET)
    public List<Items> get(HttpServletRequest httpServletRequest, @CookieValue("JSESSIONID") String sessionId) {
        System.out.println("\n\n****** STORE SERVICE JSESSIONID ******\n\n");
        System.out.println("JSESSIONID: " + sessionId);
        System.out.println("securityClient.getCurrentAccount(sessionId): " + securityClient.getCurrentAccount(sessionId));
        System.out.println("\n\n****** STORE SERVICE JSESSIONID END******\n\n");

        printRequestInfo(httpServletRequest);

        securityClient.getCurrentAccount(sessionId);
        return itemsService.get().getItems();
    }

    @RequestMapping(value = "{id}", method = GET)
    public Items getById(@PathVariable String id) {
        return itemsService.findById(id).get();
    }

    @RequestMapping(value = "/getByGroupId/{groupId}", method = GET)
    public List<Items> getByGroupId(@PathVariable String groupId) {
        return itemsService.findByGroupId(groupId).getItems();
    }

    @RequestMapping(value = "{id}", method = DELETE)
    public void delete(@PathVariable String id) {
        itemsService.delete(id);
    }


    @RequestMapping(method = GET, value = "test")
    public String test() {
        return "Test";
    }


    private void printRequestInfo(HttpServletRequest httpServletRequest) {
        System.out.println("\n\n****** STORE SERVICE Req log start ******\n\n");
        System.out.println("Request Method: " + httpServletRequest.getMethod());
        System.out.println("Request URL: " + httpServletRequest.getRequestURL().toString());
        System.out.println("Request getCookies: " + httpServletRequest.getCookies().toString());
        System.out.println("\n\n****** STORE SERVICE Req log end  ******\n\n");
    }
}
