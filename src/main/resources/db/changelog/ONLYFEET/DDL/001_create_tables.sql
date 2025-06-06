--liquibase formatted sql

--changeset onlyfeet:init-uuid-extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--changeset onlyfeet:create-users
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    keycloak_id UUID NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL CHECK (role IN ('CREATOR', 'USER', 'ADMIN')),
    stripe_account_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

--changeset onlyfeet:create-post
CREATE TABLE post (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    creator_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255),
    description TEXT,
    media_type VARCHAR(10) NOT NULL CHECK (media_type IN ('IMAGE', 'VIDEO')),
    visibility VARCHAR(20) NOT NULL DEFAULT 'SUBSCRIBERS' CHECK (visibility IN ('PUBLIC', 'SUBSCRIBERS')),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

--changeset onlyfeet:create-media-file
CREATE TABLE media_file (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    post_id UUID NOT NULL REFERENCES post(id) ON DELETE CASCADE,
    file_url TEXT NOT NULL,
    mime_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT now()
);

--changeset onlyfeet:create-subscription
CREATE TABLE subscription (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    creator_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    start_date TIMESTAMP NOT NULL DEFAULT now(),
    end_date TIMESTAMP NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    stripe_subscription_id VARCHAR(255),
    UNIQUE (user_id, creator_id)
);

--changeset onlyfeet:create-payment
CREATE TABLE payment (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    creator_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    amount_cents INT NOT NULL,
    currency VARCHAR(10) DEFAULT 'ron',
    stripe_payment_intent_id VARCHAR(255),
    stripe_session_id VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID', 'FAILED')),
    created_at TIMESTAMP DEFAULT now()
);

--changeset onlyfeet:add-indexes
CREATE INDEX idx_post_creator ON post(creator_id);
CREATE INDEX idx_subscription_user_creator ON subscription(user_id, creator_id);
