import 'package:flutter/material.dart';

class VideoPage extends StatefulWidget {
  @override
  _VideoPageState createState() => _VideoPageState();
}

class _VideoPageState extends State<VideoPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          "视频",
          style: TextStyle(color: Colors.black,fontWeight: FontWeight.bold,
),
        ),
        elevation: 0,
      ),
      body: Center(child: Text("视频"),),
    );
  }
}
