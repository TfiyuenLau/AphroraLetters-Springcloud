FROM node:18.16.0 as build-stage

WORKDIR /admin-pure

RUN corepack enable

RUN corepack prepare pnpm@7.32.1 --activate

RUN npm config set registry https://registry.npmmirror.com

COPY .npmrc package.json pnpm-lock.yaml ./

RUN pnpm install --frozen-lockfile

COPY . .

RUN pnpm build
