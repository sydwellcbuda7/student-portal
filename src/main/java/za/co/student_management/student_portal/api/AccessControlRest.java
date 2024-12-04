package za.co.student_management.student_portal.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import za.co.student_management.student_portal.control.Authentication;
import za.co.student_management.student_portal.control.Credentials;
import za.co.student_management.student_portal.service.AccessControlService;
import za.co.student_management.student_portal.transfer_object.ChangePasswordTo;
import za.co.student_management.student_portal.transfer_object.SessionContextTo;

import java.util.Set;

@RestController
@RequestMapping(path = "/access-control")
@RequiredArgsConstructor
public class AccessControlRest {

    private final AccessControlService accessControlService;

    @Operation(
            summary = "Sign in a user",
            description = "Authenticate a user using their credentials and return an authentication token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully authenticated",
                            content = @Content(schema = @Schema(implementation = Authentication.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials provided"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PermitAll
    @PostMapping(path = "/sign-in",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Authentication loginUser(@RequestBody @Valid Credentials credentials) {
        return accessControlService.loginUser(credentials);
    }

    @Operation(
            summary = "Change user password",
            description = "Allows a user to change their password by providing the credentials",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password successfully changed"),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials details provided"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PermitAll
    @PostMapping(path = "/change-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestBody @Valid ChangePasswordTo changePasswordTo) {
        accessControlService.changePassword(changePasswordTo);
    }

    @Operation(
            summary = "Reset user password",
            description = "Allows a user to reset their password by providing their username",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset instructions sent"),
                    @ApiResponse(responseCode = "400", description = "Invalid username provided"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PermitAll
    @PostMapping(path = "/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void resetPassword(@RequestParam(name = "username") String username) {
        accessControlService.resetPassword(username);
    }


    @Operation(description = "Get user session context")
    @PermitAll
    @GetMapping(path = "/session-context",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SessionContextTo> getUserSessionContext() {
        return accessControlService.getUserSessionContexts();
    }


}
