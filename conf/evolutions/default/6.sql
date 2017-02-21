# --- !Ups

ALTER TABLE public.odds ADD COLUMN favorita boolean;


# --- !Downs

ALTER TABLE public.odds DROP COLUMN IF EXISTS favorita;



