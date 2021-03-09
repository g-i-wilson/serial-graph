my $bytes = pack( "C*", "m0", 0x30, 0x31, 0x32 );

print( $bytes );
#print( unpack( "C*", $bytes ) );