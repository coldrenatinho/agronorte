# Build stage
FROM node:20-alpine AS build
WORKDIR /app

# Cache dependencies
COPY package.json package-lock.json* ./
RUN npm ci

# Build the app
COPY . .
RUN npm run build

# Production stage — Nginx Alpine
FROM nginx:alpine
RUN rm /usr/share/nginx/html/index.html

# Copia build do react
COPY --from=build /app/dist /usr/share/nginx/html

# Copia config otimizada do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
