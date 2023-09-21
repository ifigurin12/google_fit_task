import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:google_fit_test_task/bloc/bloc_auth/google_auth_bloc.dart';

import 'package:google_fit_test_task/ui/pages/auth_page.dart';
import 'package:google_fit_test_task/ui/pages/home_page.dart';
import 'package:google_fit_test_task/ui/pages/not_found_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          colorSchemeSeed: Colors.deepPurple,
          brightness: Brightness.dark,
          useMaterial3: true,
        ),
        initialRoute: HomePage.routeName,
        onGenerateRoute: (settings) {
          switch (settings.name) {
            case HomePage.routeName:
              return PageRouteBuilder(
                pageBuilder: (context, animation, secondaryAnimation) =>
                    MultiBlocProvider(
                  providers: [
                    BlocProvider<GoogleAuthBloc>(
                      create: (context) =>
                          GoogleAuthBloc()..add(GoogleIsSignedInEvent()),
                    ),
                  ],
                  child: HomePage(),
                ),
              );
            case AuthPage.routeName:
              return PageRouteBuilder(
                pageBuilder: (context, animation, secondaryAnimation) =>
                    BlocProvider<GoogleAuthBloc>(
                  create: (context) => GoogleAuthBloc(),
                  child: AuthPage(),
                ),
              );
            case NotFoundPage.routeName:
              return PageRouteBuilder(
                pageBuilder: (context, animation, secondaryAnimation) =>
                    NotFoundPage(),
              );
          }
        });
  }
}
