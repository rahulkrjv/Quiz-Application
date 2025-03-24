# Google OAuth and Email Configuration

## Set up Google OAuth
1. **Create a project in Google Cloud Console**  
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Click on "Select a project" and create a new project

2. **Enable OAuth 2.0**  
   - Navigate to "APIs & Services" > "Credentials"
   - Click "Create Credentials" > "OAuth Client ID"
   - Configure the consent screen and save changes

3. **Download `client_secret.json` and place it in your project**  
   - In "OAuth 2.0 Client IDs", click on the created client ID
   - Download the `client_secret.json` file
   - Place it in your project directory

4. **Configure authorized redirect URIs**  
   - In the OAuth settings, add your application's redirect URIs
   - Example: `http://localhost:3000/auth/google/callback`

## Configure Email Settings
1. **Update the EmailService with your email credentials**  
   - Modify the email service configuration to include your SMTP settings

2. **If using Gmail, generate an App Password**  
   - Go to your Google Account settings
   - Enable "2-Step Verification"
   - Generate an App Password for your email service
   - Use the generated password in your email configuration
