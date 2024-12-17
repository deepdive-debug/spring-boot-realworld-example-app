package io.spring.api.comment.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonRootName;
@JsonRootName("comment")
public record NewCommentParam(
	@NotBlank(message = "can't be empty") String body
) {
}
