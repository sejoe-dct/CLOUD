INSERT INTO users (user_login_id, user_password, user_name, user_nickname, user_email)
    VALUE ( 'a111' , 'a111' , '사람1' , '사람1','a111@gmail.com' );
INSERT INTO users (user_login_id, user_password, user_name, user_nickname, user_email)
    VALUE ( 'a222' , 'a222' , '사람2' , '사람2','a222@gmail.com' );
INSERT INTO users (user_login_id, user_password, user_name, user_nickname, user_email)
    VALUE ( 'a333' , 'a333' , '사람3' , '사람3','a333@gmail.com' );
INSERT INTO users (user_login_id, user_password, user_name, user_nickname, user_email)
    VALUE ( 'a444' , 'a444' , '사람4' , '사람4','a444@gmail.com' );


--
INSERT INTO location_type ( location_type)
    VALUE ( '서울' );
INSERT INTO location_type ( location_type)
    VALUE ( '부산' );
INSERT INTO location_type ( location_type)
    VALUE ( '대구' );
INSERT INTO location_type ( location_type)
    VALUE ( '창원' );

--
INSERT INTO food_type ( food_type)
    VALUE ( '양식' );
INSERT INTO food_type ( food_type)
    VALUE ( '중식' );
INSERT INTO food_type ( food_type)
    VALUE ( '일식' );
INSERT INTO food_type ( food_type)
    VALUE ( '한식' );


--
INSERT INTO food_board ( food_address , food_info , food_preview , food_rate ,food_picture, food_title , food_type_id , location_type_id , user_id)
    VALUE ( '서울역' , '스파게티 전문점' , '맛있다.' , 3.0 ,'사진경로', '스파게티 전문점' , 1 , 1 , 1);
INSERT INTO food_board ( food_address , food_info , food_preview , food_rate ,food_picture, food_title , food_type_id , location_type_id , user_id)
    VALUE ( '부산역' , '짬뽕 전문점' , '맛있다.' , 3.5 , '사진경로' ,'짬뽕 전문점', 2 , 2 , 2);
INSERT INTO food_board ( food_address , food_info , food_preview , food_rate ,food_picture, food_title , food_type_id , location_type_id , user_id)
    VALUE ( '대구역' , '스시 전문점"' , '맛있다.' , 4.1,'사진경로' , '스시 전문점' , 3 , 3 , 3);
INSERT INTO food_board ( food_address , food_info , food_preview , food_rate ,food_picture, food_title , food_type_id , location_type_id , user_id)
    VALUE ( '창원역' , '파전 전문점"' , '맛있다.' , 4.1 ,'사진경로', '파전 전문점' , 4 , 4 , 4);


--
INSERT INTO festival_board ( fstvl_nm ,opar, fstvl_start_date,fstvl_end_date	,
                             fstvl_co ,mnnst ,auspc_instt	,suprt_instt	,phone_number,
                             homepage_url	,relate_info	,rdnmadr,lnmadr	,latitude,longitude	,
                             reference_date	,instt_code,instt_nm , festival_picture , festival_info,
                             location_type_id , user_id )
    VALUE('벚꽃 축제', '서울 강남' ,'2022-07-01', '2022-08-01',
          '벚꽃 축재', '정부' , '정부' , '정부' , '051-123-4567' ,
          'www.seoul.com' , '벚꽃 축제 정부 주관' , '강남 도로명 주소' , '강남 지번 주소' , '35.12401153' , '126.8815859',
          '2022-04-01' , '제공기관코드' ,'제공기관명' , '사진경로', '"벚꽃 축제" ',
          1, 1);


INSERT INTO festival_board ( fstvl_nm ,opar, fstvl_start_date,fstvl_end_date	,
                            fstvl_co ,mnnst ,auspc_instt	,suprt_instt	,phone_number,
                            homepage_url	,relate_info	,rdnmadr,lnmadr	,latitude,longitude	,
                            reference_date	,instt_code,instt_nm , festival_picture , festival_info,
                             location_type_id , user_id )
    VALUE('모래 축제', '부산 해운대구' ,'2022-06-01', '2022-07-01',
    '모래 축재', '정부' , '정부' , '정부' , '051-123-4567' ,
    'www.busan.com' , '모래 축제 정부 주관' , '해운대 도로명 주소' , '해운대구 지번 주소' , '37.47823402' , '126.9515012',
    '2022-04-01' , '제공기관코드' ,'제공기관명' , '사진경로', '"모래축제" ',
    2, 2);



INSERT INTO festival_board ( fstvl_nm ,opar, fstvl_start_date,fstvl_end_date	,
                             fstvl_co ,mnnst ,auspc_instt	,suprt_instt	,phone_number,
                             homepage_url	,relate_info	,rdnmadr,lnmadr	,latitude,longitude	,
                             reference_date	,instt_code,instt_nm , festival_picture , festival_info,
                             location_type_id , user_id )
    VALUE('대구 축제', '대구 동성로' ,'2022-07-01', '2022-08-01',
          '대구 축재', '정부' , '정부' , '정부' , '051-123-4567' ,
          'www.daegu.com' , '대구 축제 정부 주관' , '대구 도로명 주소' , '대구 지번 주소' , '35.1071746' , '126.8231387',
          '2022-04-01' , '제공기관코드' ,'제공기관명' , '사진경로', '대구 축제',
          3, 3);

INSERT INTO festival_board ( fstvl_nm ,opar, fstvl_start_date,fstvl_end_date	,
                             fstvl_co ,mnnst ,auspc_instt	,suprt_instt	,phone_number,
                             homepage_url	,relate_info	,rdnmadr,lnmadr	,latitude,longitude	,
                             reference_date	,instt_code,instt_nm , festival_picture , festival_info,
                             location_type_id , user_id )
    VALUE('창원 축제', '창원 어디구' ,'2022-06-05', '2022-07-01',
          '창원 축재', '정부' , '정부' , '정부' , '051-123-4567' ,
          'www.chang.com' , '창원 축제 정부 주관' , '창원 도로명 주소' , '창원 지번 주소' , '35.13849504' , '126.828739',
          '2022-04-01' , '제공기관코드' ,'제공기관명' , '사진경로', '"창원축제" ',
          4, 4);




--
INSERT INTO food_cmt (food_cmt , food_board_id , user_id)
    VALUE('음식 굿1' , 1 , 1 );
INSERT INTO food_cmt (food_cmt , food_board_id , user_id)
    VALUE('음식 굿2', 2 , 2 );
INSERT INTO food_cmt (food_cmt , food_board_id , user_id)
    VALUE('음식 굿3' , 3 , 3 );
INSERT INTO food_cmt (food_cmt , food_board_id , user_id)
    VALUE('음식 굿4' , 4 , 4 );


INSERT INTO festival_cmt (festival_cmt , festival_board_id , user_id)
    VALUE('행사 굿1' , 1 , 1 );
INSERT INTO festival_cmt (festival_cmt , festival_board_id , user_id)
    VALUE('행사 굿1' , 2 , 2 );
INSERT INTO festival_cmt (festival_cmt , festival_board_id , user_id)
    VALUE('행사 굿1' , 3 , 3 );
INSERT INTO festival_cmt (festival_cmt , festival_board_id , user_id)
    VALUE('행사 굿1' , 4 , 4 );