insert into quiz ( id, text, title, username ) VALUES ( 1000, 'What depicts the Java logo?', 'Java quiz', 'testuser' );
insert into quiz_options ( quiz_id, options ) VALUES ( 1000, 'wrong' );
insert into quiz_options ( quiz_id, options ) VALUES ( 1000, 'wrong' );
insert into quiz_options ( quiz_id, options ) VALUES ( 1000, 'correct' );
insert into quiz_answer ( quiz_id, answer ) VALUES ( 1000, 2 );