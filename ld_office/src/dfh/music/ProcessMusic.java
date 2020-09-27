package dfh.music;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class ProcessMusic {


	public static void playMusic(){
		AudioInputStream m_audioInputStream = null;
		SourceDataLine m_line = null;
		AudioFormat audioFormat = null;
		try {
			m_audioInputStream = AudioSystem.getAudioInputStream(ProcessMusic.class.getResourceAsStream("alert2.mp3"));
			audioFormat = m_audioInputStream.getFormat();

			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				AudioFormat newFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED, audioFormat
								.getSampleRate(), 16,
						audioFormat.getChannels(),
						audioFormat.getChannels() * 2, audioFormat
								.getSampleRate(), false);
				System.out.println("Converting audio format to " + newFormat);
				AudioInputStream newStream = AudioSystem.getAudioInputStream(
						newFormat, m_audioInputStream);
				audioFormat = newFormat;
				m_audioInputStream = newStream;
			}

			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			m_line = (SourceDataLine) AudioSystem.getLine(info);
			m_line.open(audioFormat, m_line.getBufferSize());
			m_line.start();

			int bufferSize = (int) audioFormat.getSampleRate()
					* audioFormat.getFrameSize();
			byte[] buffer = new byte[bufferSize];

			int bytesRead = 0;
			while (bytesRead >= 0) {
				bytesRead = m_audioInputStream.read(buffer, 0, buffer.length);
				if (bytesRead >= 0) {
					m_line.write(buffer, 0, bytesRead);
				}
			}
			m_line.drain();
			m_line.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
