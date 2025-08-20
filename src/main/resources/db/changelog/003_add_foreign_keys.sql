--liquibase formatted sql
-- changeset table:002

ALTER TABLE "user"
    ADD CONSTRAINT fk_user_curator
        FOREIGN KEY (curator_id) REFERENCES curator (id);

ALTER TABLE event
    ADD CONSTRAINT fk_event_curator
        FOREIGN KEY (curator_id) REFERENCES curator (id);

ALTER TABLE user_on_event
    ADD CONSTRAINT fk_uoe_user FOREIGN KEY (user_id) REFERENCES "user" (id),
    ADD CONSTRAINT fk_uoe_event FOREIGN KEY (event_id) REFERENCES event (id);

ALTER TABLE feedback
    ADD CONSTRAINT fk_feedback_user FOREIGN KEY (user_id) REFERENCES "user" (id);

ALTER TABLE comment
    ADD CONSTRAINT fk_comment_feedback FOREIGN KEY (feedback_id) REFERENCES feedback (id);

ALTER TABLE messages
    ADD CONSTRAINT fk_msg_curator FOREIGN KEY (curator_id) REFERENCES curator (id),
    ADD CONSTRAINT fk_msg_user FOREIGN KEY (user_id) REFERENCES "user" (id);
