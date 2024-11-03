package r16a.Athena.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import r16a.Athena.config.Jwt.JwtUtil;
import r16a.Athena.models.dto.ClientRestricted;
import r16a.Athena.services.ClientService;

@AllArgsConstructor
@Controller
public class ViewController {
    private final ClientService clientService;
    private final JwtUtil jwtUtil;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("{clientId}/login")
    public String login(@PathVariable String clientId, Model model) {
        ClientRestricted client = clientService.getRestrictedById(Integer.valueOf(clientId));
        model.addAttribute("serviceConsumer", client.getName());
        return "login";
    }

    @GetMapping("{clientId}/signup")
    public String signup(@PathVariable String clientId, Model model) {
        ClientRestricted client = clientService.getRestrictedById(Integer.valueOf(clientId));
        model.addAttribute("serviceConsumer", client.getName());
        return "signup";
    }

    @GetMapping("{clientId}/password-recovery")
    public String passwordRecoveryLinkSender(@PathVariable String clientId,Model model) {
        ClientRestricted client = clientService.getRestrictedById(Integer.valueOf(clientId));
        model.addAttribute("serviceConsumer", client.getName());
        model.addAttribute("onPasswordRecoveryResetProcess", false);
        model.addAttribute("userEmail", null);
        model.addAttribute("error", null);
        return "password-recovery";
    }

    @GetMapping("{clientId}/password-recovery/{token}")
    public String passwordRecovery(@PathVariable String clientId, @PathVariable String token, Model model) {
        try {
            ClientRestricted client = clientService.getRestrictedById(Integer.valueOf(clientId));
            String userEmail = jwtUtil.validateToken(token);

            model.addAttribute("serviceConsumer", client.getName());
            model.addAttribute("onPasswordRecoveryResetProcess", true);
            model.addAttribute("userEmail", userEmail);
        } catch (Exception e) {
            model.addAttribute("onPasswordRecoveryResetProcess", false);
            model.addAttribute("error", "Invalid url");
        }

        return "password-recovery";
    }
}
