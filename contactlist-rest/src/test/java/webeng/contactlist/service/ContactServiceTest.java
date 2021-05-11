package webeng.contactlist.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import webeng.contactlist.SampleContactsAdder;
import webeng.contactlist.model.Contact;
import webeng.contactlist.model.ContactListEntry;
import webeng.contactlist.model.ContactRepository;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContactServiceTest {

    ContactService service;

    ContactServiceTest() throws IOException {
        var mapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        var testContactsFile = SampleContactsAdder.class.getResource(SampleContactsAdder.JSON_FILE);
        var testContacts = mapper.readValue(testContactsFile, new TypeReference<List<Contact>>() {});

        // create fake (mock) repo that returns the sample contacts from the file
        var repo = mock(ContactRepository.class);
        when(repo.findAll()).thenReturn(testContacts);
        service = new ContactService(repo);
    }

    @Test
    void contactListName() {
        var contactList = service.getContactList(null);
        assertNotNull(contactList);
        assertFalse(contactList.isEmpty());
        assertEquals("Mabel Guppy", contactList.get(0).getName());
    }

    @Test
    void search() {
        var results = service.getContactList("Engineer");
        assertEquals(Set.of("Graeme Impett", "Chilton Treversh"),
                results.stream().map(ContactListEntry::getName).collect(toSet()));
    }

    @Test
    void searchIgnoresCase() {
        var results = service.getContactList("mO");
        assertEquals(Set.of("Moll Mullarkey", "Seana Burberye"), // one in name, one in email
                results.stream().map(ContactListEntry::getName).collect(toSet()));
    }
}

