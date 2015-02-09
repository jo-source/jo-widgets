/*
 * Copyright (c) 2015, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class DocBookPostProcessor {

	private DocBookPostProcessor() {}

	public static void main(final String[] args) throws Exception {

		if (args.length != 2) {
			//CHECKSTYLE:OFF
			System.out.println("Usage: DocBookPostProcessor <source> <destination>");
			//CHECKSTYLE:ON
		}

		final BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));

		final FileWriter writer = new FileWriter(new File(args[1]));

		boolean lineBreakNext = false;
		String line = "";
		while (line != null) {
			line = reader.readLine();
			if (line == null) {
				break;
			}
			if (lineBreakNext && !line.contains("</programlisting>")){
				writer.write("\n");
			}
			
			if (line.contains("<programlisting language=\"xml\">")) {
				line = line.replace(
						"<programlisting language=\"xml\">",
						"<programlisting language=\"xml\" linenumbering=\"numbered\">");
				writer.write(line);
				lineBreakNext = false;
			}
			else if (line.contains("<programlisting language=\"java\">")) {
				line = line.replace(
						"<programlisting language=\"java\">",
						"<programlisting language=\"java\" linenumbering=\"numbered\">");
				writer.write(line);
				lineBreakNext = false;
			}
			else{
				lineBreakNext = true;
				writer.write(line);
			}	
		}

		reader.close();
		writer.close();
	}
}
