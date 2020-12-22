import 'dart:async';

import 'package:bloc/bloc.dart';
import 'package:light_music_flutter/models/banner/banner_result.dart';
import 'package:light_music_flutter/repositories/itf/home_repository.dart';
import 'package:meta/meta.dart';

part 'home_event.dart';
part 'home_state.dart';

class HomeBloc extends Bloc<HomeEvent, HomeState> {
  HomeBloc({@required this.repository}) : super(HomeLoading());

  final HomeWidgetRepository repository;

  @override
  Stream<HomeState> mapEventToState(
    HomeEvent event,
  ) async* {
    final List<dynamic> datas = List();
    final BannerResult bannerResult = await repository.loadBanner();
    datas.add(bannerResult);
    yield HomeDataLoaded(datas);
  }
}
