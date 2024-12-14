package r16a.Athena.controllers.view;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import r16a.Athena.config.Jwt.JwtUtil;
import r16a.Athena.models.dto.ClientRestricted;
import r16a.Athena.services.ClientService;

@AllArgsConstructor
@Controller
public class ViewController {
    private final ClientService clientService;
    private final JwtUtil jwtUtil;

    @ModelAttribute
    public void addServiceConsumerToModel(@PathVariable(required = false) String clientId, Model model) {
        if (clientId != null) {
            ClientRestricted client = clientService.getRestrictedById(Integer.valueOf(clientId));
            model.addAttribute("serviceConsumer", client.getName());
            model.addAttribute("serviceConsumerIcon", client.getIcon());
        }
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("{clientId}/login")
    public String login() {
        return "login";
    }

    @GetMapping("{clientId}/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("{clientId}/register-in-new-client")
    public String confirmSignup() {
        return "register-in-new-client";
    }

    @GetMapping("{clientId}/confirm-signup-new-client/{token}")
    public String registerInNewClient(@PathVariable String token, Model model) {
        try {
            String userEmail = jwtUtil.validateToken(token);
            model.addAttribute("userEmail", userEmail);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid url");
        }

        return "confirm-signup-new-client";
    }

    @GetMapping("{clientId}/password-recovery")
    public String passwordRecoveryLinkSender(Model model) {
        model.addAttribute("onPasswordRecoveryResetProcess", false);
        model.addAttribute("userEmail", null);
        model.addAttribute("error", null);
        return "password-recovery";
    }

    @GetMapping("{clientId}/password-recovery/{token}")
    public String passwordRecovery(@PathVariable String token, Model model) {
        try {
            String userEmail = jwtUtil.validateToken(token);
            model.addAttribute("onPasswordRecoveryResetProcess", true);
            model.addAttribute("userEmail", userEmail);
        } catch (Exception e) {
            model.addAttribute("onPasswordRecoveryResetProcess", false);
            model.addAttribute("error", "Invalid url");
        }

        return "password-recovery";
    }
}
