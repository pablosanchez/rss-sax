# rss-sax
Small program that uses SAX approach to parse this [RSS feed](http://www.europapress.es/rss/rss.aspx) and generate a XML file containing the news titles

It consists of five classes:
1. [RSSSax](https://github.com/pablosanchez/rss-sax/blob/master/src/rsssax/RSSSax.java): main class, it starts **ChannelHandler**, prints out on screen news information and generates the XML file
2. [ChannelHandler](https://github.com/pablosanchez/rss-sax/blob/master/src/rsssax/ChannelHandler.java): parses the channel information and gives control to **NewsHandler** for every `<item>` it encounters. Also stores a list of news
3. [Channel](https://github.com/pablosanchez/rss-sax/blob/master/src/rsssax/Channel.java): model class to store channel information
4. [NewsHandler](https://github.com/pablosanchez/rss-sax/blob/master/src/rsssax/NewsHandler.java): parses every `<item>` of the document, storing its information on a **ItemNew** object
5. [ItemNew](https://github.com/pablosanchez/rss-sax/blob/master/src/rsssax/ItemNew.java): model class to store news information

Developed as a [Master MIMO](http://www.web.upsa.es/mimo/) practice