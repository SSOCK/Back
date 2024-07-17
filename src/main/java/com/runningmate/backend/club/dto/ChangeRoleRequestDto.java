package com.runningmate.backend.club.dto;

import com.runningmate.backend.club.ClubRole;
import com.runningmate.backend.validation.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChangeRoleRequestDto {
    @NotNull(message = "New role must not be null")
    @ValidEnum(enumClass = ClubRole.class, message = "Invalid role. Allowed roles are MEMBER, OWNER, MODERATOR.")
    private ClubRole clubRole;
}
