package skatn.remindmeback.subject.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import skatn.remindmeback.subject.contoller.dto.TagListResponse;
import skatn.remindmeback.subject.service.TagQueryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagQueryService tagQueryService;

    @GetMapping
    public TagListResponse getTags(@RequestParam(name = "size", defaultValue = "60") int size,
                                   @RequestParam(name = "keyword", required = false) String keyword) {

        return new TagListResponse(tagQueryService.getTags(size, keyword));
    }
}
