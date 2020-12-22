import 'package:flutter/material.dart';
import 'package:light_music_flutter/models/personalizedmusiclist/personalized_music_list.dart';
import 'package:light_music_flutter/net/api/api_service.dart';

class PersonailzedMusicListWidget extends StatefulWidget {
  @override
  _PersonailzedMusicListWidgetState createState() =>
      _PersonailzedMusicListWidgetState();
}

class _PersonailzedMusicListWidgetState
    extends State<PersonailzedMusicListWidget> {
  List<PersonalizedMusicList> _list = List();

  @override
  void initState() {
    var data =
        ApiService(context: context).getPersonalizedMusicList().then((value) {
      setState(() {
        _list = value.result;
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 150,
      child: ListView.builder(
        scrollDirection: Axis.horizontal,
        itemCount: _list.length,
        itemBuilder: (context, index) {
          return _createItem(_list[index]);
        },
      ),
    );
  }

  Widget _createItem(PersonalizedMusicList musicList) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Container(
          width: 120,
          height: 120,
          child: ClipRRect(
            borderRadius: BorderRadius.circular(10),
            child: FadeInImage.assetNetwork(
              placeholder: "images/base_icon_default_cover.png",
              image: musicList.picUrl,
              width: 120,
              height: 100,
              fit: BoxFit.fitWidth,
            ),
          ),
        ),
        Padding(
            padding: EdgeInsets.all(5),
            child: Container(
              width: 120,
              child: Text(
                musicList.name,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
                style: TextStyle(color: Colors.black, fontSize: 12),
              ),
            ))
      ],
    );
  }
}
