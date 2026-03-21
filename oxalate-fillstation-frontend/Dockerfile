FROM node:22-alpine AS build

WORKDIR /app

COPY package.json yarn.lock .yarnrc.yml ./
COPY .yarn/ .yarn/

RUN corepack enable && yarn install --immutable

COPY . .

ARG BUILD_VERSION=0.0.0-dev
ENV BUILD_VERSION=${BUILD_VERSION}

RUN yarn build

FROM nginx:1.29-alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
