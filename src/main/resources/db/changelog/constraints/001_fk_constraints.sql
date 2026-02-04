-- liquibase formatted sql

-- changeset zetta-dev:2
ALTER TABLE categories
ADD CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE tasks
ADD CONSTRAINT fk_task_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE tasks
ADD CONSTRAINT fk_task_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL;

ALTER TABLE subtasks
ADD CONSTRAINT fk_subtask_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE;

ALTER TABLE tags
ADD CONSTRAINT fk_tag_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE task_tags
ADD CONSTRAINT fk_tasktags_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE;

ALTER TABLE task_tags
ADD CONSTRAINT fk_tasktags_tag FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE;