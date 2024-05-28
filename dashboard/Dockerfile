# Use an official Node runtime as a parent image
FROM node:18

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json to the container
COPY package*.json ./

# Install app dependencies
RUN npm install --legacy-peer-deps

# Copy the rest of the application code to the container
COPY . .

#RUN rm angular.json
#RUN mv angular_prod.json angular.json

# Build the Angular app
RUN npm run build

# Expose the port the app runs on
EXPOSE 4200

# Define the command to run the application
CMD ["npm", "start"]
