# Example 03: UI Dialog Form Coordination

## 1. Problem Scenario
In an enterprise user registration dialog, multiple UI widgets depend on each other's state:
- An input for `Username` (must be at least 4 characters, alphanumeric).
- An input for `Password` (must be at least 8 characters, containing at least one digit).
- A checkbox for `Terms & Conditions` (must be checked).
- A `Submit` button (must only be enabled when all three conditions are satisfied).
- An `Error Banner` (displays dynamic context-sensitive error hints as the user interacts with fields).

## 2. Bad Design (Direct Widget-to-Widget Coupling)
- Every widget holds concrete references to several sibling widgets.
- For example, `BadUsernameField` holds references to `BadSubmitButton`, `BadErrorBanner`, `BadPasswordField`, and `BadTermsCheckbox`.
- When the user types a character in the username box, the text field must inspect the password field and checkbox to determine if the button should be enabled.
- Result: UI components cannot be reused in another dialog or screen because they are hardcoded to the specific layout and components of this registration form.

## 3. Good Design (Mediator Pattern)
- `FormDialogMediator`: Interface declaring the event notification protocol (`notify(UIComponent sender, FormEvent event)`), validation query (`isFormValid()`), and action trigger (`submit()`).
- `RegistrationDialogMediator`: Concrete mediator encapsulating all cross-widget validation rules, error messaging, and button state toggling.
- `UIComponent` subclasses (`TextInputComponent`, `CheckboxComponent`, `ButtonComponent`, `BannerComponent`): Completely generic, decoupled UI building blocks.
  - When text is typed in a text field, it emits `mediator.notify(this, FormEvent.VALUE_CHANGED)`.
  - The text field has zero references to any button, banner, or checkbox.
  - The components can be reused across any dialog or form throughout the application.

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/17-mediator/examples/03-example/diagram.mmd).

## 5. Execution Flow
1. `RegistrationDialogMediator` is constructed and registers the UI components.
2. Initially, the submit button is disabled, and the error banner indicates "Please complete registration form".
3. User enters username `"john_doe"`. `TextInputComponent` notifies the mediator.
4. Mediator evaluates validity: password is missing -> Banner displays "Password must be at least 8 chars", submit button remains disabled.
5. User enters password `"SecurePass123"`. Mediator validates: terms not checked -> Banner displays "You must accept terms", submit button remains disabled.
6. User checks Terms checkbox. Mediator validates: all conditions satisfied -> Error banner clears, submit button enables.
7. User clicks Submit button -> Mediator triggers user account creation and updates banner to "Registration Successful".
