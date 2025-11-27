# API d'Authentification Blood Link

## Endpoints disponibles

### 1. Inscription (Sign Up)
**POST** `/api/v1/auth/signUp`

Permet d'inscrire un nouvel utilisateur (Donor, Doctor ou Blood Bank).

#### Request Body (Donor)
```json
{
  "name": "NK",
  "surname": "Wilfried",
  "email": "noubissie.k.w@gmail.com",
  "password": "Test123",
  "phoneNumber": "690232120",
  "userRole": "donor",
  "bloodType": "A_POSITIVE",
  "LastDonationDate": "2024-01-15"
}
```

#### Request Body (Doctor)
```json
{
  "name": "Audrey",
  "surname": "Noubissie",
  "email": "audrey.noubissie@gmail.com",
  "password": "Test123",
  "phoneNumber": "671126291",
  "userRole": "doctor",
  "speciality": "Cardiology",
  "hospitalName": "CHU"
}
```

#### Request Body (Blood Bank)
```json
{
  "email": "contact@bloodbank.com",
  "password": "Test123",
  "phoneNumber": "690232120",
  "userRole": "bloodBank",
  "bloodBankName": "Central Blood Bank",
  "address": "Mendong, Yaoundé"
}
```

#### Response (Success - 200)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "noubissie.k.w@gmail.com",
  "role": "donor"
}
```

#### Response (Error - 400)
```json
{
  "message": "This email is already used"
}
```

---

### 2. Connexion (Login)
**POST** `/api/v1/auth/logIn`

Permet de se connecter avec email et mot de passe.

#### Request Body
```json
{
  "email": "noubissie.k.w@gmail.com",
  "password": "Test123"
}
```

#### Response (Success - 200)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "noubissie.k.w@gmail.com",
  "role": "donor"
}
```

---

### 3. Récupérer l'utilisateur connecté
**GET** `/api/v1/auth/current-user`

Permet de récupérer les informations de l'utilisateur actuellement connecté.

#### Headers
```
Authorization: Bearer {token}
```

#### Response (Success - 200)
```json
{
  "email": "noubissie.k.w@gmail.com",
  "phoneNumber": "690232120",
  "userRole": "donor",
  "name": "NK",
  "surname": "Wilfried",
  "bloodType": "A_POSITIVE",
  "lastDonationDate": "2024-01-15"
}
```

---

## Valeurs acceptées

### UserRole
- `donor` - Donneur de sang
- `doctor` - Médecin
- `bloodBank` - Banque de sang

### BloodType
- `A_POSITIVE`, `A_NEGATIVE`
- `B_POSITIVE`, `B_NEGATIVE`
- `AB_POSITIVE`, `AB_NEGATIVE`
- `O_POSITIVE`, `O_NEGATIVE`

---

## Notes importantes

1. **⚠️ MODE DÉVELOPPEMENT** : La sécurité est actuellement **DÉSACTIVÉE**. Tous les endpoints sont accessibles sans authentification. Aucun token JWT n'est requis pour les requêtes.

2. **Token JWT** : Après connexion ou inscription, vous recevez un token JWT, mais il n'est **PAS nécessaire** de l'utiliser pour le moment (sécurité désactivée).

3. **Format de date** : Les dates doivent être au format ISO 8601 : `YYYY-MM-DD`

4. **Validation** :
   - Email doit être unique
   - Numéro de téléphone doit être unique
   - Nom de banque de sang doit être unique (pour les Blood Banks)

5. **CORS** : Le backend accepte les requêtes de toutes les origines (configuré pour le développement).

---

## ⚠️ Important pour la production

Avant de déployer en production, vous devrez réactiver la sécurité dans `SecurityConfiguration.java` en décommentant les lignes du filtre JWT et en configurant les autorisations appropriées.
