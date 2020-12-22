import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:light_music_flutter/blocs/home/home_bloc.dart';
import 'package:light_music_flutter/repositories/impl/home_net_repository.dart';
import 'package:light_music_flutter/repositories/itf/home_repository.dart';

/// Created by lipeilin
/// on 2020/12/21
class BlocWrapper extends StatefulWidget {
  final Widget child;

  BlocWrapper({this.child});

  @override
  _BlocWrapperState createState() => _BlocWrapperState();
}

class _BlocWrapperState extends State<BlocWrapper> {
  final HomeWidgetRepository repository = HomeNetRepository();

  @override
  Widget build(BuildContext context) {
    return MultiBlocProvider(
      providers: [
        BlocProvider<HomeBloc>(
          create: (context) => HomeBloc(repository: repository),
        ),
      ],
      child: widget.child,
    );
  }
}
