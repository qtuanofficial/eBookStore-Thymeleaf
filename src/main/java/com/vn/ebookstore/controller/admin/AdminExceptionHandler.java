@ControllerAdvice(basePackages = "com.vn.ebookstore.controller.admin")
public class AdminExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleError(Exception e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/admin/dashboard";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied() {
        return "redirect:/login";
    }
}
