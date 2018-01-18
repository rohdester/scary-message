package dk.bearsoft.scaries.controller;

import dk.bearsoft.scaries.model.ScaryMessage;
import dk.bearsoft.scaries.repository.ScaryMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
public class MessageController {

    private final ScaryMessageRepository scaryMessageRepository;

    @Autowired
    public MessageController(ScaryMessageRepository scaryMessageRepository) {
        this.scaryMessageRepository = scaryMessageRepository;
    }

    @RequestMapping(path = "/messages", method = RequestMethod.GET)
    public Collection<ScaryMessage> geAll() {
        return scaryMessageRepository.findAll();
    }

    @RequestMapping(path = "/messages", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody ScaryMessage message) {
        // In a real-world solution I would probably use some validation service here
        // In a larger app I might not use a repo directly in a controller - depending on architecture
        scaryMessageRepository.save(message);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(message.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(path = "/messages/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable long id) {
        // Here I would also like to do some validity checking.
        scaryMessageRepository.deleteById(id);
        URI location = ServletUriComponentsBuilder
                .fromPath("/messages")
                .build().toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(path = "/messages/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> modify(@PathVariable long id, @RequestBody ScaryMessage input) {

        Optional<ScaryMessage> optionalScaryMessage = scaryMessageRepository.findById(id);
        if (optionalScaryMessage.isPresent()) {
            ScaryMessage scaryMessage = optionalScaryMessage.get();
            scaryMessage.setMessage(input.getMessage());
            scaryMessageRepository.save(scaryMessage);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

}
