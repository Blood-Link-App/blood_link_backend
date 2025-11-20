# Authentication service:
- RegisterUser (RegisterRequest request)
- Login(LoginRequest request)
- Logout()
- UpdateUserInformation
  * Password

# BloodRequest service:
- Create bloodRequest
- Doctor send a blood request to a blood bank
- Blood bank creates an alert triggering by bloodRequest and automatically sends a notification to compatible donors.


# Alert service:
- Create an alert
   * Create a verify Alert's attribute
- delete an alert
- update status
- get alert by Status 
- get alert by ...

# Medical Profile Service  _(only a connected donor can do this)_
- Methods : 
  * Create Medical Profile 
  * Update Medical Profile
  * Get medical profile 
  * // do I need the method "delete"

- What does this trigger to ?
  * check vital signs
  * check health questions (male)
  * check personal infos 
 
# Donation Request service


# Notification Service