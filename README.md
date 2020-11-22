יצרתי פרוייקט ויישמתי את כל מה שביקשת (הוספתי mockMVC ו swagger לקלות ההפעלה) עבדתי מול kafka  
התקנתי סרבר קפקא (אפשר כאמור היה לעבוד עם ה docker perform, אבל גם אותו צריך להוריד :) כך שזה אותו זמן)https://github.com/shaishalom/messageSample1
יש להיכנס לספרייה בה השרת קפקא לפתוח cmd ולהפעיל את הפקודות הבאות (בחלונות נפרדים)
2 התורים הם (שיש להגדיר)bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic topic1
bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic topic2

וכמובן להעלות את השרת bin\windows\zookeeper-server-start.bat config\zookeeper.properties
bin\windows\kafka-server-start.bat config\server.properties

ואז כאמור להריץ את ה MessageApplicationכתבתי גם Junits
כדי להפעיל אתה צריך להריץ את MessageApplication , ואז בסוואגר תשלח הודעה (עם 2 שדות)ההודעה תישלח ב Json, מוצר ל XML, תישלח לתור הראשון , תחכה לקבלה שלו מהתור, ומשם תישלח לתור השני , שייחכה עד שיגיע ורק אז יציג אותו על המסך (כביכול אנחנו הופכים את התהליך לסינכרוני דרך 2 טופיקים)  http://localhost:8080/swagger-ui.htm  

http://localhost:8080/swagger-ui.html#/message-controller/listingsUsingPOST


 

ותקבל את התוצאה הרצויה:
 

יש להריץ את ה Test  כ Junit ותקבל את התוצאה הרצויה (אפשר להריץ דרך url(דרך  mock) מה שיותר נכון , ואפשר ישירות)ב 2 הדרכים עובד:

 
 
בהצלחה
שי





