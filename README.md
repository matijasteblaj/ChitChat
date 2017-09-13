# ChitChat
Projekt ChitChat za predmet Programiranje 2. 
Klepetalnik se povezuje s strežnikom na naslovu http://chitchat.andrej.com/. Zažene se ga z datoteko ChitChat.java.

# Implementirana funkcionalnost
- Nastavljiv vzdevek
- Prijava in odjava s strežnika s klikom na gumb
- Spisek vseh prijavljenih ljudi, ki se posodobi vsako sekundo
- Pošiljanje osebnih in globalnih sporočil
- Zavihki za sledenje večim pogovorom, ob prejemu novega sporočila v neizbranem zavihku se ustrezen zavihek pobarva
- Ob prejemu sporocila se odpre ali posodobi ustrezen zavihek
- Ob odjavi se vsi zavihki (razen "Server") avtomatično zaprejo
- Robot RandomReader, ki v zavihek "Server" piše naključno izbrane vrstice iz datoteke Tekst.txt v mapi src
- Ob zaprtju aplikacije se uporabnik avtomatsko odjavi iz strežnika (če se že ni prej sam)
