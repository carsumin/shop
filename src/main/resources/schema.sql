-- Ensure flag column can store longer values
ALTER TABLE public."member"
    ALTER COLUMN flag TYPE varchar(20);
