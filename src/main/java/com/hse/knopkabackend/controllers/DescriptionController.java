package com.hse.knopkabackend.controllers;

import com.hse.knopkabackend.DTO.DescriptionDTO;
import com.hse.knopkabackend.models.Description;
import com.hse.knopkabackend.services.DescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "api/v1/description")
public class DescriptionController {

    private final DescriptionService descriptionService;

    @Autowired
    public DescriptionController(DescriptionService descriptionService) {
        this.descriptionService = descriptionService;
    }

    @GetMapping("/{knopkaId}")
    public DescriptionDTO getDescription(@PathVariable("knopkaId") Long knopkaId,
                                         @RequestParam("knopkaUserId") Long knopkaUserId,
                                         @RequestHeader String token) {
        Description description = descriptionService.getDescription(knopkaId, token, knopkaUserId);
        return new DescriptionDTO(description.getText(), description.getEncodedImage(), description.getTags());
    }

//    @GetMapping("/tag")
//    public List<Long> getKnopkaIdsByTag(@RequestParam("tag") String tag,
//                                        @RequestParam("knopkaUserId") Long knopkaUserId,
//                                        @RequestHeader String token){
//        List<Description> descriptionList = descriptionService.getDescriptionsByTag(tag, token, knopkaUserId);
//        return descriptionList.stream().map(Description::getKnopkaWithThisKnopkaId).collect(Collectors.toList());
//    }


    @PutMapping(path = "/{descriptionKnopkaId}")
    public void updateDescription(@PathVariable("descriptionKnopkaId") Long descriptionKnopkaId,
                                  @RequestBody DescriptionDTO descriptionDTO,
                                  @RequestHeader String token) {
        if (descriptionDTO.getText() != null)
            descriptionService.updateDescriptionText(descriptionKnopkaId, descriptionDTO.getText(), token);
        if (descriptionDTO.getImage() != null)
            descriptionService.updateDescriptionImage(descriptionKnopkaId, descriptionDTO.getImage(), token);
        if (descriptionDTO.getTags() != null)
            descriptionService.updateDescriptionTags(descriptionKnopkaId, descriptionDTO.getTags(), token);
    }

}
