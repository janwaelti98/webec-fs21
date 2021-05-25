package webeng.contactlist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webeng.contactlist.model.Contact;
import webeng.contactlist.service.ContactService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactsRestController {

    private final ContactService service;

    public ContactsRestController(ContactService service) {
        this.service = service;
    }

    @GetMapping
    public List<Contact> getAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Contact> getContact(@PathVariable int id) {
        var contact = service.findContact(id).orElse(null);
        if (contact == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(contact);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable int id) {
        var contact = service.findContact(id).orElse(null);
        if (contact == null) {
            return ResponseEntity.notFound().build();
        } else {
            service.delete(contact);
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<String> replaceContact(@PathVariable int id,
                                                 @RequestBody Contact newContact) {
        if (newContact.getId() != null && newContact.getId() != id) {
            return ResponseEntity.badRequest().body("invalid ID");
        }
        var original = service.findContact(id).orElse(null);
        if (original == null) {
            return ResponseEntity.notFound().build();
        } else {
            service.replace(id, newContact);
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> addContact(@RequestBody Contact contact) {
        if (contact.getId() != null) {
            return ResponseEntity.badRequest().body("ID not permitted");
        }
        var saved = service.addOrUpdate(contact);
        return ResponseEntity.created(URI.create("/api/contacts/" + saved.getId())).build();
    }
}
