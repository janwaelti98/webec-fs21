package webeng.contactlist.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import webeng.contactlist.model.Contact;
import webeng.contactlist.model.ContactRepository;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class ContactsRestControllerTest {

    @Autowired
    TestRestTemplate rest;
    @Autowired
    ContactRepository repo;

    @Test
    public void getAll() {
        var response = rest.exchange("/api/contacts", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Contact>>() {});
        var contacts = response.getBody();
        assertEquals(30, contacts.size());
        var first = contacts.get(0);
        assertEquals("Mabel", first.getFirstName());
        assertEquals("Guppy", first.getLastName());
    }

    @Test
    public void getContact() {
        // determine ID first
        var id = repo.findAll().stream().findFirst().get().getId();

        var contact = rest.getForObject("/api/contacts/" + id, Contact.class);
        assertEquals("Mabel", contact.getFirstName());
        assertEquals("Guppy", contact.getLastName());
    }

    @Test
    @DirtiesContext
    public void deleteContact() {
        var listBefore = rest.exchange("/api/contacts", HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Contact>>() {}).getBody();
        assertEquals(30, listBefore.size());
        var id = listBefore.get(0).getId();

        rest.delete("/api/contacts/" + id);

        var listAfter = rest.getForObject("/api/contacts", List.class);
        assertEquals(29, listAfter.size());

        var response = rest.getForEntity("/api/contacts/" + id, Contact.class);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    public void replaceContact() {
        // determine ID first
        var id = repo.findAll().stream().findFirst().get().getId();

        var contactBefore = rest.getForObject("/api/contacts/" + id, Contact.class);
        assertEquals("Mabel", contactBefore.getFirstName());
        assertEquals("Guppy", contactBefore.getLastName());

        var newContact = "{ \"firstName\": \"Tester\" }";   // use hand-written string instead of
        // automatic conversion, which adds "id" field
        var request = RequestEntity.put("/api/contacts/" + id)
                .contentType(APPLICATION_JSON)
                .body(newContact);
        var response = rest.exchange(request, String.class);
        assertNull(response.getBody());
        assertEquals(NO_CONTENT, response.getStatusCode());

        var contactAfter = rest.getForObject("/api/contacts/" + id, Contact.class);
        assertEquals("Tester", contactAfter.getFirstName());
        assertNull(contactAfter.getLastName());
    }

    @Test
    @DirtiesContext
    public void addContact() {
        // determine max ID first to infer next one (this is not super robust...)
        var id = repo.findAll().stream().mapToInt(Contact::getId).max().getAsInt() + 1;

        var responseBefore = rest.getForEntity("/api/contacts/" + id, Contact.class);
        assertEquals(NOT_FOUND, responseBefore.getStatusCode());
        assertNull(responseBefore.getBody());

        var newContact = "{ \"firstName\": \"Mike\", \"lastName\": \"Miller\" }";
        var request = RequestEntity.post("/api/contacts")
                .contentType(APPLICATION_JSON)
                .body(newContact);
        var response = rest.exchange(request, String.class);
        assertNull(response.getBody());
        assertEquals(CREATED, response.getStatusCode());

        var location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertEquals("/api/contacts/" + id, location.toString());

        var responseAfter = rest.getForEntity("/api/contacts/" + id, Contact.class);
        assertEquals(OK, responseAfter.getStatusCode());
        var contactAfter = responseAfter.getBody();
        assertNotNull(contactAfter);
        assertEquals("Mike", contactAfter.getFirstName());
        assertEquals("Miller", contactAfter.getLastName());
        assertNull(contactAfter.getCompany());
        assertNull(contactAfter.getJobTitle());
        assertEquals(emptyList(), contactAfter.getEmail());
        assertEquals(emptyList(), contactAfter.getPhone());
    }
}
