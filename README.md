# Simple Real-Time chat in Android using AWS Amplify

Follow this steps

Step 1: https://docs.amplify.aws/lib/project-setup/prereq/q/platform/android Project Setup

Step 2: https://docs.amplify.aws/lib/project-setup/create-application/q/platform/android Create Your Application

Step 3: https://docs.amplify.aws/lib/datastore/getting-started/q/platform/android Setup Datastore API. While adding API using `amplfy add api` or `amplify update api` choose :

  1.`# Choose the default authorization type for the API` - `Cognito User Pool Default Settings`  

  2.`# Configure conflict detection?` - `Yes`

  3.`# Do you want to edit the schema now?  Yes`

The CLI should open this GraphQL schema in your text editor, otherwise open the `amplify/backend/api/myapi/schema.graphql` file and paste the content written in https://github.com/sili42/Simple-Chat-Box-in-Android-using-AWS-Amplify/blob/master/schema.graphql 

Step 4: `amplify codegen models`

Step 5: `amplify push`. choose the default option

Step 6: Copy the files in .Java and res files from https://github.com/sili42/Simple-Chat-Box-in-Android-using-AWS-Amplify/tree/master/app/src/main into your project and then Run.

*Make Sure to change your app package name at app level build.Gradle file, which is inside app folder*
